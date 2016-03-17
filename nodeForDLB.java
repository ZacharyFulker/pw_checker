
/**
 *
 * @author Zach
 */
public class nodeForDLB {
	public nodeForDLB horizontal;
	public nodeForDLB vertical;
        public char value;

	public nodeForDLB(char nodeValue, nodeForDLB horizontal, nodeForDLB vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
                this.value = nodeValue;
	}
}